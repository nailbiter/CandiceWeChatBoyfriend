# The Silly Echo Bot for WeChat - Ruby version
#
# The purpose of this code is to show the basics of creating chatbots for the WeChat platform.
# It creates a simple backend that echoes back the messages it receives from users.
#
# See my article on Chatbots Magazine for details:
# https://chatbotsmagazine.com/building-chatbots-for-wechat-part-1-dba8f160349
#
# Tested with:
# Ruby 2.2.3
# Sinatra 1.4.8 - http://www.sinatrarb.com
# Nokogiri 1.7.1 - http://www.nokogiri.org
#
# Try it on a sandbox account: http://admin.wechat.com/debug/sandbox
# Works well with ngrok for local testing: https://ngrok.com
#
# License: MIT (https://opensource.org/licenses/MIT) (C) Edoardo Nosotti, 2017

# Import dependencies
require 'sinatra'
require 'nokogiri'
require 'time'
require 'digest/sha1'

# Set the shared token (must be set in the WeChat sandobox console, too)
WECHAT_TOKEN = "EQ200MXv3MckvBbv"

# A class providing methods to handle GET and POST requests
class WeChatApiResource
  def initialize(token)
    @token = token
  end

  # Check if the message XML is valid, this simple bot handles TEXT messages only!
  # To learn more about the supported types of messages and how to implement them, see:
  # Common Messages: http://admin.wechat.com/wiki/index.php?title=Common_Messages
  # Event Messages: http://admin.wechat.com/wiki/index.php?title=Event-based_Messages
  # Speech Recognition Messages: http://admin.wechat.com/wiki/index.php?title=Speech_Recognition_Messages
  def validate_message(message)
    return (
      message != nil &&
      message.xpath('//MsgType').length > 0 &&
      message.xpath('//Content').length > 0 &&
      message.xpath('//MsgType')[0].inner_html == 'text'
    )
  end

  # Format the reply according to the WeChat XML format for synchronous replies,
  # see: http://admin.wechat.com/wiki/index.php?title=Callback_Messages
  def format_message(original_message, content)
    to_user = original_message.xpath('//FromUserName')[0].inner_html # From and To must be inverted in replies ;)
    from_user = original_message.xpath('//ToUserName')[0].inner_html # Same as above!
    timestamp = Time.new.to_i
    return %(
      <xml>
      <ToUserName><![CDATA[#{to_user}]]></ToUserName>
      <FromUserName><![CDATA[#{from_user}]]></FromUserName>
      <CreateTime>#{timestamp}</CreateTime>
      <MsgType><![CDATA[text]]></MsgType>
      <Content><![CDATA[#{content}]]></Content>
      </xml>
    )
  end

  # The WeChat server will issue a GET request in order to verify the chatbot backend server upon configuration.
  # See: http://admin.wechat.com/wiki/index.php?title=Getting_Started#Step_2._Verify_validity_of_the_URL
  # and: http://admin.wechat.com/wiki/index.php?title=Message_Authentication
  def get(request, params)
    # Get the parameters from the query string
    signature = params['signature'] || ''
    timestamp = params['timestamp'] || ''
    nonce = params['nonce'] || ''
    echostr = params['echostr'] || ''

    # Compute the signature (note that the shared token is used too)
    verification_elements = [@token, timestamp, nonce]
    verification_elements = verification_elements.sort
    verification_string = verification_elements.join('')
    verification_string = Digest::SHA1.hexdigest(verification_string)

    # If the signature is correct, output the same "echostr" provided by the WeChat server as a parameter
    if signature == verification_string
      return echostr
    end

    return ''
  end

  # Messages will be POSTed from the WeChat server to the chatbot backend server,
  # see: http://admin.wechat.com/wiki/index.php?title=Common_Messages
  def post(request, params)
    # Parse the WeChat message XML format
    message = Nokogiri::XML(request.body.read.to_s)

    # If the message is valid, echo it back to the user or send an error message.
    # Some kind of response, even an empty one, is *REQUIRED* by WeChat
    # within the mandatory timeout limit of 5 seconds.
    # Otherwise, the user will see an error in the app.
    if validate_message(message)
      content = "You typed: " << message.xpath('//Content')[0].inner_html
      return format_message(message, content)
    end

    return 'Message was sent in a wrong format.'
  end
end

# Make Sinatra bind to port 8000
set :port, 8000

# Map routes (see: http://www.sinatrarb.com/intro.html#Routes)
get '/wechat' do
  resource = WeChatApiResource.new(WECHAT_TOKEN)
  return resource.get(request, params)
end

post '/wechat' do
  resource = WeChatApiResource.new(WECHAT_TOKEN)
  resource.post(request, params)
end

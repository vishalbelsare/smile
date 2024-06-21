import React, { } from 'react'
import "./Chat.css"
import ChatHeader from './ChatHeader'
import MessageInput from './MessageInput'
import MessageList from './MessageList'

export default function Chat({
    style,
    userId,
    messages,
    sendMessage,
    showTypingIndicator,
    title = "SmileServe - Llama3",
    placeholder = "Type prompt here",
}) {
    return (
        <div className="chat-container" style={style}>
            <div className="message-container">
                <ChatHeader>
                    {title}
                </ChatHeader>
                <MessageList
                    messages={messages}
                    userId={userId}
                    showTypingIndicator={showTypingIndicator}
                />
                <MessageInput
                    onSendMessage={sendMessage}
                    placeholder={placeholder}
                />
            </div>
        </div>
    )
}
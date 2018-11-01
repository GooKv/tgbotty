import React, { Component, Fragment } from "react";
import { withRouter } from "react-router-dom";
import { MessageList } from "./MessageList";
import { ChatHeader } from "./ChatHeader";

const getChatMessages = chatId =>
  fetch(`/view/${chatId}`)
    .then(response => response.json())
    .catch(console.error);

class ChatWindow extends Component {
  state = {
    chatName: "",
    messages: []
  };

  componentDidMount() {
    this.getChatMessages();
  }

  getChatMessages = () => {
    const {
      params: { chatId }
    } = this.props.match;

    getChatMessages(chatId).then(response =>
      this.setState({
        chatName: response.displayName,
        messages: response.messagesDtoList
      })
    );
  };

  render() {
    const { chatName, messages } = this.state;

    return (
      <Fragment>
        <ChatHeader header={chatName} />
        <MessageList messages={messages} />
      </Fragment>
    );
  }
}

const wrappedComponent = withRouter(ChatWindow);

export { wrappedComponent as ChatWindow };

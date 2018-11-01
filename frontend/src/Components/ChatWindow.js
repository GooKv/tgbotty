import React, { Component, Fragment } from "react";
import { withRouter } from "react-router-dom";
import { MessageList } from "./MessageList";
import { ChatHeader } from "./ChatHeader";
import { SendMessagePanel } from "./SendMessagePanel";
import { message } from "antd";

const getChatMessages = chatId =>
  fetch(`/view/${chatId}`)
    .then(response => response.json())
    .catch(error => {
      console.error(error);
      message.error("Не удалось загрузить чат");
      throw error;
    });

const defaultState = {
  chatName: "",
  messages: []
};

class ChatWindow extends Component {
  state = defaultState;

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
      }).catch(() => this.setState(defaultState))
    );
  };

  render() {
    const { chatName, messages } = this.state;

    return (
      <Fragment>
        <ChatHeader header={chatName} />
        <MessageList messages={messages} />
        <SendMessagePanel />
      </Fragment>
    );
  }
}

const wrappedComponent = withRouter(ChatWindow);

export { wrappedComponent as ChatWindow };

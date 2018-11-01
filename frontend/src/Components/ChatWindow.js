import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { MessageList } from "./MessageList";
import { ChatHeader } from "./ChatHeader";
import { SendMessagePanel } from "./SendMessagePanel";
import { message, Layout } from "antd";

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
  messages: [],
  canTalk: false,
  avatarUrl: null
};

class ChatWindow extends Component {
  state = defaultState;

  componentDidMount() {
    const getChatPeriodiocally = () => {
      this.getChatMessages();
      this.getChatRequestTimerId = setTimeout(getChatPeriodiocally, 1000);
    };

    this.getChatMessages();

    this.getChatRequestTimerId = setTimeout(getChatPeriodiocally, 1000);
  }

  componentWillUnmount() {
    clearTimeout(this.getChatRequestTimerId);
  }

  getChatMessages = () => {
    const {
      params: { chatId }
    } = this.props.match;

    getChatMessages(chatId)
      .then(response =>
        this.setState({
          chatName: response.displayName,
          messages: response.messagesDtoList,
          canTalk: response.canTalk,
          avatarUrl: response.avatarUrl
        })
      )
      .catch(() => this.setState(defaultState));
  };

  render() {
    const { chatName, messages, canTalk, avatarUrl } = this.state;

    return (
      <Layout className="chat-layout">
        <ChatHeader header={chatName} />
        <MessageList messages={messages} avatarUrl={avatarUrl} />
        <SendMessagePanel canTalk={canTalk} />
      </Layout>
    );
  }
}

const wrappedComponent = withRouter(ChatWindow);

export { wrappedComponent as ChatWindow };

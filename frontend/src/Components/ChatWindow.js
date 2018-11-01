import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Layout } from "antd";

const { Content } = Layout;

const Message = ({ message }) => (
  <div
    style={{
      height: "100px",
      background: "black",
      margin: "10px 0",
      color: "white"
    }}
  >
    {message}
  </div>
);

const getChatMessages = chatId =>
  fetch(`/view/${chatId}`).then(response => response.json()).catch(console.error);

class ChatWindow extends Component {
  state = {
    messages: []
  };

  componentDidMount() {
    this.getChatMessages();
  }

  componentDidUpdate() {
    this.getChatMessages();
  }

  getChatMessages = () => {
    const {
      params: { chatId }
    } = this.props.match;

    getChatMessages(chatId).then(messages => this.setState({ messages }));
  };

  hello = () => {
    const body = new FormData();
    body.append("name", "whoever u are");

    fetch("/hi", { method: "POST", body })
      .then(response => response.json())
      .then(({ message }) => {
        this.setState({ message: message });
      })
      .catch(console.error);
  };

  render() {
    return (
      <Content className="main-content">
        {Array(100)
          .fill()
          .map((_, index) => (
            <Message key={index} message={this.state.message} />
          ))}
      </Content>
    );
  }
}

const wrappedChatWindow = withRouter(ChatWindow);

export { wrappedChatWindow as ChatWindow };

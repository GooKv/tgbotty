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

class MessageList extends Component {
  render() {
    const { messages } = this.props;
    return (
      <Content className="main-content">
        {messages.map((it, index) => (
          <Message key={index} message={it.message} />
        ))}
      </Content>
    );
  }
}

const wrappedComponent = withRouter(MessageList);

export { wrappedComponent as MessageList };

import React, { Component } from "react";
import { Layout } from "antd";

const { Header } = Layout;

class ChatHeader extends Component {
  render() {
    return (
      <Header className="main-header">
        <h2>Header</h2>
      </Header>
    );
  }
}

export { ChatHeader };

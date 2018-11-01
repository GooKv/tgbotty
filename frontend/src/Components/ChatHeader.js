import React, { Component } from "react";
import { Layout } from "antd";

const { Header } = Layout;

class ChatHeader extends Component {
  render() {
    const { header } = this.props;
    return (
      <Header className="main-header">
        <h2>{header}</h2>
      </Header>
    );
  }
}

export { ChatHeader };

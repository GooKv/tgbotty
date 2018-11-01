import React, { Component } from "react";
import { Layout, Input } from "antd";

const { Footer } = Layout;
const { TextArea } = Input;

export class SendMessagePanel extends Component {
  render() {
    return (
      <Footer className="main-footer">
        <TextArea rows={4} placeholder="Введите сообщение" />
      </Footer>
    );
  }
}

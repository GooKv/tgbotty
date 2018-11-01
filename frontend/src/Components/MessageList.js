import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Layout, List } from "antd";
import { Avatar } from "./Avatar";

const { Content } = Layout;

const isUser = user => user.senderType === "CUSTOMER";

class MessageList extends Component {
  render() {
    const { messages, avatarUrl } = this.props;

    return (
      <Content className="main-content">
        <List
          itemLayout="horizontal"
          dataSource={messages}
          renderItem={item => (
            <List.Item>
              <div className={`message ${!isUser(item) && "message-right"}`}>
                <div className="message-avatar">
                  <Avatar user={item} avatarUrl={avatarUrl} />
                </div>
                <div className="message-content">
                  <div className="message-sender">{item.sender || ""}</div>
                  <div className="message-text">{item.message}</div>
                  <div className="message-date">{item.time}</div>
                </div>
              </div>
            </List.Item>
          )}
        />
      </Content>
    );
  }
}

const wrappedComponent = withRouter(MessageList);

export { wrappedComponent as MessageList };

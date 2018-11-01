import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Layout, List, Avatar } from "antd";

const { Content } = Layout;

const userAvatarStyle = { backgroundColor: "#87d068" };
const employeeAvatarStyle = { backgroundColor: "#87d068" };
const botAvatarStyle = { backgroundColor: "#4286f4" };

const BotAvatar = () => <Avatar style={botAvatarStyle} icon="robot" />;
const UserAvatar = () => <Avatar style={userAvatarStyle} icon="user" />;
const EmployeeAvatar = () => <Avatar style={employeeAvatarStyle}>ВЫ</Avatar>;

const renderAvatar = user => {
  switch (user.senderType) {
    case "BOT":
      return <BotAvatar />;
    case "CUSTOMER":
      return <UserAvatar />;
    case "SUPPORT":
    default:
      return <EmployeeAvatar />;
  }
};

const isUser = user => user.senderType === "CUSTOMER";

class MessageList extends Component {
  render() {
    const { messages } = this.props;

    return (
      <Content className="main-content">
        <List
          itemLayout="horizontal"
          dataSource={messages}
          renderItem={item => (
            <List.Item>
              <div className={`message ${!isUser(item) && "message-right"}`}>
                <div className="message-avatar">{renderAvatar(item)}</div>
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

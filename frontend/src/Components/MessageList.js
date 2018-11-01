import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Layout, List, Avatar } from "antd";

const { Content } = Layout;

const userAvatarStyle = { backgroundColor: "#87d068" };
const employeeAvatarStyle = { backgroundColor: "#87d068" };
const botAvatarStyle = { backgroundColor: "#4286f4" };

const BotAvatar = () => (
  <Avatar style={botAvatarStyle} icon="robot" />
);
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
              <List.Item.Meta
                className={!isUser(item) && "message-right"}
                avatar={renderAvatar(item)}
                title={item.sender || ""}
                description={item.message}
              />
            </List.Item>
          )}
        />
      </Content>
    );
  }
}

const wrappedComponent = withRouter(MessageList);

export { wrappedComponent as MessageList };

import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Layout, List, Avatar } from "antd";

const { Content } = Layout;

const userAvatarStyle = { backgroundColor: "#87d068" };
const botAvatarStyle = { backgroundColor: "#05af32" };

const isUser = it => it.sender !== "bot";

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
                avatar={
                  <Avatar
                    style={isUser(item) ? userAvatarStyle : botAvatarStyle}
                  >
                    {item.sender.charAt(0)}
                  </Avatar>
                }
                title={item.sender}
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

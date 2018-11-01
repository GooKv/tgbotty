import React, { Component } from "react";
import { Link } from "react-router-dom";
import { Layout, Icon, message, List } from "antd";

const { Sider } = Layout;

const getChats = () =>
  fetch("/view")
    .then(response => response.json())
    .catch(error => {
      console.error(error);
      message.error("Не удалось получить список чатов");
      throw error;
    });

const menuItemStyle = {
  width: "100%",
  height: "30px",
  background: "yellowgreen",
  margin: "10px 0px"
};

const defaultState = {
  chatList: []
};

class Menu extends Component {
  state = defaultState;

  componentDidMount() {
    getChats()
      .then(chatList => {
        this.setState({ chatList });
      })
      .catch(() => this.setState(defaultState));
  }

  render() {
    const { chatList } = this.state;

    return (
      <Sider
        className="main-sider"
        theme="light"
        width={400}
        collapsible
        trigger={<Icon type="menu-fold" theme="outlined" />}
      >
        <List
          itemLayout="horizontal"
          dataSource={chatList}
          renderItem={chat => (
            <Link to={`/chat/${chat.id}`} key={chat.id}>
              <List.Item style={menuItemStyle}>{chat.displayName}</List.Item>
            </Link>
          )}
        />
      </Sider>
    );
  }
}

export { Menu };

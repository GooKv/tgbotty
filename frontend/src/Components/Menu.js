import React, { Component } from "react";
import { Link } from "react-router-dom";
import { Layout, Icon, message } from "antd";

const { Sider } = Layout;

const MenuItem = ({ chat }) => (
  <div
    style={{
      width: "100%",
      height: "30px",
      background: "yellowgreen",
      margin: "10px 0px"
    }}
  >
    {chat}
  </div>
);

const getChats = () =>
  fetch("/view")
    .then(response => response.json())
    .catch(error => {
      console.error(error);
      message.error("Не удалось получить список чатов");
      throw error;
    });

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
        width={400}
        collapsible
        trigger={<Icon type="menu-fold" theme="outlined" />}
      >
        {chatList.map(chat => (
          <Link to={`/chat/${chat.id}`} key={chat.id}>
            <MenuItem chat={chat.displayName} />
          </Link>
        ))}
      </Sider>
    );
  }
}

export { Menu };

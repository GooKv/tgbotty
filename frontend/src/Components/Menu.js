import React, { Component } from "react";
import { Link } from "react-router-dom";
import { Layout, Icon } from "antd";

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

const getChats = () => fetch("/view").then(response => response.json());

class Menu extends Component {
  state = {
    chatList: []
  };

  componentDidMount() {
    getChats()
      .then(chatList => {
        this.setState({ chatList });
      })
      .catch(console.error);
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
          <Link to={`/chat/${chat.id}`} key={chat.id} >
            <MenuItem chat={chat.displayName} />
          </Link>
        ))}
      </Sider>
    );
  }
}

export { Menu };

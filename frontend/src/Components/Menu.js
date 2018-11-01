import React, { Component } from "react";
import { Link } from "react-router-dom";
import { Layout, Icon, message, List } from "antd";
import { Avatar } from "./Avatar";

const { Sider } = Layout;

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
    const getChatPeriodiocally = () => {
      this.getChats();
      this.getChatRequestTimerId = setTimeout(getChatPeriodiocally, 1000);
    };

    this.getChats();

    this.getChatRequestTimerId = setTimeout(getChatPeriodiocally, 1000);
  }

  componentWillUnmount() {
    clearTimeout(this.getChatRequestTimerId);
  }

  getChats = () =>
    getChats()
      .then(chatList => {
        this.setState({ chatList });
      })
      .catch(() => this.setState(defaultState));

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
        <h3 className="menu-header">Список чатов</h3>
        <div className="menu-content">
          <List
            itemLayout="horizontal"
            dataSource={chatList}
            renderItem={chat => (
              <Link
                to={`/chat/${chat.id}`}
                key={chat.id}
                className="menu-item-wrapper"
              >
                <List.Item>
                  <div className="menu-item">
                    <div className="message-avatar">
                    <Avatar user={chat.lastMessage} />
                  </div>
                  <div className="message-content">
                    <div className="message-sender">
                      {chat.lastMessage.sender || ""}
                    </div>
                    <div className="message-text">
                      {chat.lastMessage.message}
                    </div>
                    <div className="message-date">{chat.lastMessage.time}</div>
                  </div>
                  </div>
                </List.Item>
              </Link>
            )}
          />
        </div>
      </Sider>
    );
  }
}

export { Menu };

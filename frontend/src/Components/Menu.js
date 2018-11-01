import React, { Component } from "react";
import { Link, withRouter } from "react-router-dom";
import { Layout, Icon, message, List, Tabs } from "antd";
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

const getRequests = () =>
  fetch("/view/request/")
    .then(response => response.json())
    .catch(error => {
      console.error(error);
      message.error("Не удалось получить список запросов");
      throw error;
    });

const defaultState = {
  chatList: [],
  requestList: []
};

class Menu extends Component {
  state = defaultState;

  componentDidMount() {
    const getChatPeriodiocally = () => {
      this.getChats();
      this.getRequests();
      this.getChatRequestTimerId = setTimeout(getChatPeriodiocally, 1000);
    };

    this.getChats();
    this.getRequests();

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

  getRequests = () =>
    getRequests()
      .then(requestList => {
        this.setState({ requestList });
      })
      .catch(() => this.setState(defaultState));

  onRequestClick = requestId => {
    const { history } = this.props;
    fetch(`/view/request/${requestId}`)
      .then(response => response.json())
      .then(chatId => history.push(`/chat/${chatId}`));
  };

  render() {
    const { chatList, requestList } = this.state;

    return (
      <Sider
        className="main-sider"
        theme="light"
        width={400}
        collapsible
        trigger={<Icon type="menu-fold" theme="outlined" />}
      >
        <Tabs defaultActiveKey="1">
          <Tabs.TabPane tab="Список чатов" key="1">
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
                        </div>
                      </div>
                    </List.Item>
                  </Link>
                )}
              />
            </div>
          </Tabs.TabPane>
          <Tabs.TabPane tab="Список заявок" key="2">
            <div className="menu-content">
              <List
                itemLayout="horizontal"
                dataSource={requestList}
                renderItem={request => (
                  <List.Item
                    key={request.requestId}
                    onClick={() => this.onRequestClick(request.requestId)}
                  >
                    <div className="menu-item">
                      <div className="message-content">
                        <div className="message-sender">
                          {request.requestNumber}
                        </div>
                        <div className="message-text">{request.text}</div>
                      </div>
                    </div>
                  </List.Item>
                )}
              />
            </div>
          </Tabs.TabPane>
        </Tabs>
      </Sider>
    );
  }
}

const wrappedComponent = withRouter(Menu);

export { wrappedComponent as Menu };

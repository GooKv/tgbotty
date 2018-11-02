import React, { Component } from "react";
import ReactDOM from "react-dom";
import { withRouter } from "react-router-dom";
import { Layout, List } from "antd";
import { Avatar } from "./Avatar";
import { Map } from "./Map";

const { Content } = Layout;

const isUser = user => user.senderType === "CUSTOMER";

class MessageList extends Component {
  state = {
    scrollToBottom: true
  };

  componentDidUpdate() {
    if (this.state.scrollToBottom) {
      this.wrapperRef.scrollTop = this.wrapperRef.scrollHeight;
    }
  }

  getRef = ref => (this.wrapperRef = ReactDOM.findDOMNode(ref));

  onScroll = () => {
    if (this.wrapperRef.scrollTop === this.wrapperRef.scrollHeight) {
      this.setState({ scrollToBottom: true });
    } else {
      this.setState({ scrollToBottom: false });
    }
  };

  render() {
    const { messages, avatarUrl } = this.props;

    return (
      <Content
        className="main-content"
        ref={this.getRef}
        onScroll={this.onScroll}
      >
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
                  {item.location ? (
                    <Map coordinates={item.message} />
                  ) : (
                    <div className="message-text">{item.message}</div>
                  )}
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

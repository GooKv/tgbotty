import React, { Component } from "react";
import { HashRouter as Router, Route, Switch } from "react-router-dom";
import { Layout } from "antd";
import { Menu, ChatWindow } from "./Components";
import "./App.scss";

const { Footer, Content } = Layout;

const renderChatWindow = ({ match }) => (
  <ChatWindow key={match.params.chatId} />
);

class App extends Component {
  render() {
    return (
      <Router>
        <Layout className="main-layout">
          <Menu />
          <Content>
            <Layout className="main-layout">
              <Switch>
                <Route path="/chat/:chatId" render={renderChatWindow} />
              </Switch>
              <Footer className="main-footer">Footer</Footer>
            </Layout>
          </Content>
        </Layout>
      </Router>
    );
  }
}

export default App;

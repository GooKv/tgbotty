import React, { Component } from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import { Layout } from "antd";
import { Menu, ChatWindow, ChatHeader } from "./Components";
import "./App.scss";

const { Footer, Content } = Layout;

class App extends Component {
  render() {
    return (
      <Router>
        <Layout className="main-layout">
          <Menu />
          <Content>
            <Layout className="main-layout">
              <ChatHeader />
              <Switch>
                <Route path="/chat/:chatId" component={ChatWindow} />
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

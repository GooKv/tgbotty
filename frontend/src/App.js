import React, { Component } from "react";
import { Layout, Row, Col, Icon } from "antd";
import "./App.scss";

const { Header, Footer, Content, Sider } = Layout;

const SiderItemStub = () => (
  <div
    style={{
      width: "100%",
      height: "30px",
      background: "yellowgreen",
      margin: "10px 0px"
    }}
  />
);
class App extends Component {
  state = {
    message: ""
  };

  componentDidMount() {
    this.hello();
  }

  hello = () => {
    const body = new FormData();
    body.append("name", "whoever u are");

    fetch("/hi", { method: "POST", body })
      .then(response => response.json())
      .then(({ message }) => {
        this.setState({ message: message });
      })
      .catch(console.error);
  };

  render() {
    return (
      <Layout className="main-layout">
        <Sider
          className="main-sider"
          width={400}
          collapsible
          trigger={<Icon type="menu-fold" theme="outlined" />}
        >
          {Array(100)
            .fill()
            .map((_, index) => (
              <SiderItemStub key={index} />
            ))}
        </Sider>
        <Content>
          <Layout className="main-layout">
            <Header className="main-header">
              <h2 style={{ color: "white" }}>tgbotty</h2>
            </Header>
            <Content className="main-content">
              {Array(100)
                .fill()
                .map((_, index) => (
                  <div
                    key={index}
                    style={{
                      height: "100px",
                      background: "black",
                      margin: "10px 0"
                    }}
                  />
                ))}

              <Row>
                <Col>{this.state.message}</Col>
              </Row>
            </Content>
            <Footer className="main-footer">Footer</Footer>
          </Layout>
        </Content>
      </Layout>
    );
  }
}

export default App;

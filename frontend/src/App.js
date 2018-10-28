import React, { Component } from 'react';
import { Layout, Row, Col } from 'antd';

const { Header, Footer, Content } = Layout;

class App extends Component {
  state = {
    message: '',
  };

  componentDidMount() {
    this.hello();
  }

  hello = () => {
    const body = new FormData();
    body.append('name', 'whoever u are');

    fetch('/hi', { method: 'POST', body })
      .then(response => response.json())
      .then(({ message }) => {
        this.setState({ message: message });
      })
      .catch(console.error);
  };

  render() {
    return (
      <Layout style={{ height: '100vh' }}>
        <Header>
          <h2 style={{ color: 'white' }}>tgbotty</h2>
        </Header>
        <Content>
          <Row>
            <Col>
              {this.state.message}
            </Col>
          </Row>
        </Content>
        <Footer>Footer</Footer>
      </Layout>
    );
  }
}

export default App;

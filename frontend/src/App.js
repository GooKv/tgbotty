import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

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
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>{this.state.message}</p>
        </header>
      </div>
    );
  }
}

export default App;

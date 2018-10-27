import React from 'react';
import ReactDOM from 'react-dom';
import client from './client';

class App extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            message: '',
        }
    }

    componentDidMount() {
        client({method: 'GET', path: '/hi', params: { name: "whoever u are" }}).done(response => {
            this.setState({message: response.entity.message});
        });
    }

    render() {
        return (
            <div>{this.state.message}</div>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
);
const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
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
import { React } from 'react';
import { ReactDOM } from 'react-dom';
import client from 'rest';

class App extends React.Component {

    componentDidMount() {
        client({method: 'GET', path: '/hi', parameters: { name: "whoever u are" }}).done(response => {
            this.setState({message: response.message});
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
import React, { Component } from 'react';
import { Layout, Input } from 'antd';

const { Footer } = Layout;

export class SendMessagePanel extends Component {
    render(){
        return(
            <Footer className="main-footer">
                <Input placeholder="Basic usage" />
            </Footer>
        );
    }
}
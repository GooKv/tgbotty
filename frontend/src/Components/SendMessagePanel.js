import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Layout, Input, Button, Form } from "antd";

const { Footer } = Layout;
const { TextArea } = Input;

const sendMessage = (chatId, message) => {
  if (!message || !message.trim()) {
    return Promise.resolve();
  }

  const body = new FormData();
  body.append("message", message);

  return fetch(`view/${chatId}/sendMessage`, { method: "POST", body }).catch(
    error => {
      console.error(error);
      message.error(
        "Не удалось отправить сообщение, попробуйте повторить позднее"
      );
      throw error;
    }
  );
};

class SendMessagePanel extends Component {
  constructor(props) {
    super(props);
    const {
      match: {
        params: { chatId }
      }
    } = props;
    this.sendMessage = sendMessage.bind(undefined, chatId);
  }

  handleSubmit = event => {
    const { form } = this.props;
    event.preventDefault();
    form.validateFields((err, values) => {
      if (!err) {
        this.sendMessage(values.message).then(() =>
          form.setFieldsValue({ message: null })
        );
      }
    });
  };

  render() {
    const { getFieldDecorator } = this.props.form;

    return (
      <Footer className="main-footer">
        <Form
          layout="horizontal"
          onSubmit={this.handleSubmit}
          className="send-message-form"
        >
          {getFieldDecorator("message")(
            <TextArea rows={4} placeholder="Введите сообщение" />
          )}
          <Button
            type="primary"
            size="large"
            htmlType="submit"
            onClick={this.onClick}
          >
            Отправить
          </Button>
        </Form>
      </Footer>
    );
  }
}

const wrappedComponent = withRouter(Form.create()(SendMessagePanel));

export { wrappedComponent as SendMessagePanel };

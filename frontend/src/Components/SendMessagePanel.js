import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Layout, Input, Button, Form, Popover, message } from "antd";
import { Picker } from "emoji-mart";
import "emoji-mart/css/emoji-mart.css";

const { Footer } = Layout;
const { TextArea } = Input;

const sendMessage = (chatId, messageText) => {
  if (!messageText || !messageText.trim()) {
    return Promise.resolve();
  }

  const body = new FormData();
  body.append("message", messageText);

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

const joinToChat = chatId => {
  return fetch(`view/${chatId}/startDialog`).catch(error => {
    console.error(error);
    message.error(
      "Не удалось присоединиться к диалогу, попробуйте повторить позднее"
    );
    throw error;
  });
};

const pickEmojiButtonStyle = {
  width: "32 px",
  height: "32 px",
  flexGrow: 0,
  flexShrink: 0
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
    this.joinToChat = joinToChat.bind(undefined, chatId);
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

  pickEmoji = value => {
    const { form } = this.props;
    const oldMessageValue = form.getFieldValue("message");
    const message = oldMessageValue
      ? oldMessageValue + value.native
      : value.native;
    debugger;
    form.setFieldsValue({ message });
  };

  render() {
    const {
      canTalk,
      form: { getFieldDecorator }
    } = this.props;

    return (
      <Footer className="main-footer">
        {canTalk ? (
          <Form
            layout="horizontal"
            onSubmit={this.handleSubmit}
            className="send-message-form"
          >
            {getFieldDecorator("message")(
              <TextArea rows={4} placeholder="Введите сообщение" />
            )}
            <Popover
              content={<Picker onSelect={this.pickEmoji} />}
              title="Title"
            >
              <Button
                shape="circle"
                icon="search"
                style={pickEmojiButtonStyle}
              />
            </Popover>
            <Button type="primary" size="large" htmlType="submit">
              Отправить
            </Button>
          </Form>
        ) : (
          <Button type="primary" size="large" onClick={this.joinToChat}>
            Присоединиться к чату
          </Button>
        )}
      </Footer>
    );
  }
}

const wrappedComponent = withRouter(Form.create()(SendMessagePanel));

export { wrappedComponent as SendMessagePanel };

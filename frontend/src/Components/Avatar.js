import React from "react";
import { Avatar } from "antd";

const userAvatarStyle = { backgroundColor: "#87d068" };
const employeeAvatarStyle = { backgroundColor: "#87d068" };
const botAvatarStyle = { backgroundColor: "#4286f4" };

const BotAvatar = () => <Avatar style={botAvatarStyle} icon="robot" />;
const UserAvatar = () => <Avatar style={userAvatarStyle} icon="user" />;
const EmployeeAvatar = () => <Avatar style={employeeAvatarStyle}>ВЫ</Avatar>;

const MyAvatar = ({ user }) => {
  switch (user.senderType) {
    case "BOT":
      return <BotAvatar />;
    case "CUSTOMER":
      return <UserAvatar />;
    case "SUPPORT":
    default:
      return <EmployeeAvatar />;
  }
};

export { MyAvatar as Avatar };

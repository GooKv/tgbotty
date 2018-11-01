import React from "react";
import { Avatar } from "antd";

const userAvatarStyle = { backgroundColor: "#87d068" };
const employeeAvatarStyle = { backgroundColor: "#87d068" };
const botAvatarStyle = { backgroundColor: "#4286f4" };

const BotAvatar = () => <Avatar style={botAvatarStyle} icon="robot" />;
const UserAvatar = ({ avatarUrl }) => {
  let avatarProps = {};
  if (avatarUrl) {
    avatarProps.src = avatarUrl;
  } else {
    avatarProps.icon = "user";
  }
  return <Avatar style={userAvatarStyle} {...avatarProps} />;
};
const EmployeeAvatar = () => <Avatar style={employeeAvatarStyle}>ВЫ</Avatar>;

const MyAvatar = ({ user, avatarUrl }) => {
  switch (user.senderType) {
    case "BOT":
      return <BotAvatar />;
    case "CUSTOMER":
      return <UserAvatar avatarUrl={avatarUrl} />;
    case "SUPPORT":
    default:
      return <EmployeeAvatar />;
  }
};

export { MyAvatar as Avatar };

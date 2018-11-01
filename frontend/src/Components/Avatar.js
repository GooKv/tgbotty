import React from "react";
import { Avatar } from "antd";

const BotAvatar = () => <Avatar className="bot-avatar" icon="robot" />;
const UserAvatar = ({ avatarUrl }) => {
  let avatarProps = {};
  if (avatarUrl) {
    avatarProps.src = avatarUrl;
  } else {
    avatarProps.icon = "user";
  }
  return <Avatar className="user-avatar" {...avatarProps} />;
};
const EmployeeAvatar = () => <Avatar className="employee-avatar">ВЫ</Avatar>;

const AvatarWrapper = ({ user, avatarUrl }) => {
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

export { AvatarWrapper as Avatar };

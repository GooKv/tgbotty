import React from "react";
import { Avatar } from "antd";

const userAvatarStyle = { backgroundColor: "#87d068" };
const employeeAvatarStyle = { backgroundColor: "#87d068" };
const botAvatarStyle = { backgroundColor: "#4286f4" };

const BotAvatar = () => <Avatar style={botAvatarStyle} icon="robot" />;
const UserAvatar = ({avatarUrl}) => <Avatar style={userAvatarStyle} icon={!avatarUrl &&"user"} >{avatarUrl}</Avatar>;
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

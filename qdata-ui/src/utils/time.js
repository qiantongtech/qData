import moment from "moment";
export function timeAgo(timeStr) {
  const date = new Date(timeStr);
  const now = new Date();
  const diff = (now - date) / 1000; // 秒

  if (diff < 60) {
    return moment(timeStr).format("HH:mm");
  } else if (diff < 3600) {
    return `${Math.floor(diff / 60)}分钟前`;
  } else if (diff < 86400) {
    return `${Math.floor(diff / 3600)}小时前`;
  } else if (diff < 2592000) {
    return `${Math.floor(diff / 86400)}天前`;
  } else if (diff < 31536000) {
    return `${Math.floor(diff / 2592000)}月前`;
  } else {
    return `${Math.floor(diff / 31536000)}年前`;
  }
}

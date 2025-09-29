export default function handleFilePreview(fileUrl) {
    const rpUrl = import.meta.env.VITE_RP_VIEW_URL;
    const baseUrl = import.meta.env.VITE_APP_BASE_API;
    const appUrl = window.location.origin;

    const fullUrl = `${appUrl}${baseUrl}${fileUrl.trim()}`;
    console.log(fullUrl);
    // 获取屏幕尺寸
    const screenWidth = window.screen.width;
    const screenHeight = window.screen.height;
    // 设置窗口尺寸为屏幕尺寸的一部分，例如60%
    const width = screenWidth * 0.7;
    const height = screenHeight * 0.7;
    // 计算窗口居中时的左上角位置
    const left = (screenWidth - width) / 2;
    const top = (screenHeight - height) / 2;
    // 打开新窗口并居中
    const newWindow = window.open(rpUrl + "/onlinePreview?url=" + encodeURIComponent(base64Encode(fullUrl)), "", `scrollbars=yes, width=${width}, height=${height}, top=${top}, left=${left}`);
    if (window.focus) {
        newWindow.focus();
    }
}

const base64Encode = (str) => {
    return btoa(
        encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function (match, p1) {
            return String.fromCharCode("0x" + p1);
        })
    );
};
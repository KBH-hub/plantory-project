function timeAgo(createdAt) {
    const now = new Date();
    const past = new Date(createdAt);
    const diffMs = now - past;

    const diffMinutes = Math.floor(diffMs / 60000);
    const diffHours   = Math.floor(diffMs / 3600000);
    const diffDays    = Math.floor(diffMs / 86400000);

    if (diffMinutes < 1) return "방금 전";
    if (diffMinutes < 60) return diffMinutes + "분 전";
    if (diffHours < 24)   return diffHours + "시간 전";
    if (diffDays < 7)     return diffDays + "일 전";

    const diffWeeks = Math.floor(diffDays / 7);
    return diffWeeks + "주 전";
}

function formatDate(dateString) {
    const d = new Date(dateString);
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, "0");
    const dd = String(d.getDate()).padStart(2, "0");
    const hh = String(d.getHours()).padStart(2, "0");
    const min = String(d.getMinutes()).padStart(2, "0");
    return `${yyyy}.${mm}.${dd} ${hh}:${min}`;
}

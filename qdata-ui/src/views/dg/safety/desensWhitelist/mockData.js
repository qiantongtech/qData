function pad2(n) {
  return String(n).padStart(2, "0");
}

function fmtDate(d) {
  return `${d.getFullYear()}-${pad2(d.getMonth() + 1)}-${pad2(d.getDate())}`;
}

function fmtDateTime(d) {
  return `${fmtDate(d)} ${pad2(d.getHours())}:${pad2(d.getMinutes())}:${pad2(
    d.getSeconds()
  )}`;
}

function buildCatList() {
  return [
    { id: 1, parentId: 0, code: "CAT01", name: "客户域" },
    { id: 2, parentId: 0, code: "CAT02", name: "订单域" },
    { id: 3, parentId: 0, code: "CAT03", name: "财务域" },
    { id: 11, parentId: 1, code: "CAT01-01", name: "客户信息" },
    { id: 12, parentId: 1, code: "CAT01-02", name: "联系人信息" },
    { id: 21, parentId: 2, code: "CAT02-01", name: "订单信息" },
    { id: 22, parentId: 2, code: "CAT02-02", name: "支付信息" },
    { id: 31, parentId: 3, code: "CAT03-01", name: "发票信息" },
    { id: 32, parentId: 3, code: "CAT03-02", name: "对账信息" },
  ];
}

function buildRows() {
  const now = new Date();
  const users = ["吴凡", "张三", "李四", "王五", "赵六"];
  const userAccounts = {
    吴凡: "18878945612",
    张三: "18878945613",
    李四: "18878945614",
    王五: "18878945615",
    赵六: "18878945616",
  };
  const cats = buildCatList().filter((c) => c.parentId !== 0);
  const rows = [];
  for (let i = 1; i <= 57; i++) {
    const c = cats[i % cats.length];
    const daysAgo = (i * 2) % 45;
    const d = new Date(now.getTime() - daysAgo * 24 * 60 * 60 * 1000);
    const ud = new Date(d.getTime() + (i % 7) * 60 * 60 * 1000);
    const start = new Date(d.getTime() + (i % 5) * 24 * 60 * 60 * 1000);
    const end = new Date(start.getTime() + (180 + (i % 240)) * 24 * 60 * 60 * 1000);
    const effectiveTimeRange = [fmtDate(start), fmtDate(end)];
    rows.push({
      id: i,
      whitelistName: `脱敏白名单-${i}`,
      description:
        i % 4 === 0
          ? `用于联调展示的白名单描述 ${i}，这里会更长一些以测试省略号效果。`
          : `用于联调展示的白名单描述 ${i}`,
      dataCategoryCode: c.code,
      dataCategoryName: c.name,
      effectiveAccount: users.slice(0, (i % 4) + 1),
      effectiveTimeRange,
      status: i % 4 === 0 ? "0" : "1",
      remark: i % 5 === 0 ? `备注示例 ${i}` : null,
      createBy: users[i % users.length],
      createAccount: userAccounts[users[i % users.length]] || null,
      createTime: fmtDateTime(d),
      updateBy: i % 3 === 0 ? "系统更新" : users[(i + 1) % users.length],
      updateTime: fmtDateTime(ud),
    });
  }
  return rows;
}

function getSortValue(row, key) {
  if (!key) return undefined;
  const map = {
    create_time: "createTime",
    data_category_code: "dataCategoryCode",
  };
  const prop = map[key] || key;
  return row[prop];
}

const store = {
  cats: buildCatList(),
  rows: buildRows(),
  seq: 1000,
};

export function listMockEffectiveAccounts() {
  return ["吴凡", "张三", "李四", "王五", "赵六"];
}

export function listMockDataCategory() {
  return store.cats.slice();
}

export function queryMockDesensWhitelistList(params) {
  const pageNum = Number(params?.pageNum || 1);
  const pageSize = Number(params?.pageSize || 10);
  const whitelistName = (params?.whitelistName || "").trim();
  const status =
    params?.status === 0 || params?.status ? String(params.status) : "";
  const dataCategoryCode = (params?.dataCategoryCode || "").trim();
  const orderByColumn = params?.orderByColumn || params?.orderBy;
  const isAsc = params?.isAsc || params?.order;

  let filtered = store.rows.filter((r) => {
    if (whitelistName) {
      if (!String(r.whitelistName || "").includes(whitelistName)) return false;
    }
    if (dataCategoryCode) {
      if (String(r.dataCategoryCode ?? "") !== dataCategoryCode) return false;
    }
    if (status) {
      if (String(r.status ?? "") !== status) return false;
    }
    return true;
  });

  if (orderByColumn && isAsc) {
    const dir = String(isAsc).toLowerCase().includes("desc") ? -1 : 1;
    filtered = filtered
      .slice()
      .sort((a, b) => {
        const av = getSortValue(a, orderByColumn);
        const bv = getSortValue(b, orderByColumn);
        if (av == null && bv == null) return 0;
        if (av == null) return -1 * dir;
        if (bv == null) return 1 * dir;
        if (typeof av === "number" && typeof bv === "number") return (av - bv) * dir;
        return String(av).localeCompare(String(bv)) * dir;
      });
  }

  const total = filtered.length;
  const start = (pageNum - 1) * pageSize;
  const end = start + pageSize;
  return { rows: filtered.slice(start, end), total };
}

export function getMockDesensWhitelistById(id) {
  const rid = Number(id);
  return store.rows.find((r) => Number(r.id) === rid) || null;
}

export function addMockDesensWhitelist(data) {
  store.seq += 1;
  const now = new Date();
  const cat =
    store.cats.find((c) => String(c.code) === String(data?.dataCategoryCode)) ||
    null;
  const row = {
    id: store.seq,
    whitelistName: data?.whitelistName ?? null,
    description: data?.description ?? null,
    dataCategoryCode: data?.dataCategoryCode ?? null,
    dataCategoryName: cat?.name ?? null,
    effectiveAccount: data?.effectiveAccount ?? null,
    effectiveTimeRange: data?.effectiveTimeRange ?? null,
    status: data?.status ?? "0",
    remark: data?.remark ?? null,
    createBy: "本地模拟",
    createAccount: "18800000000",
    createTime: fmtDateTime(now),
    updateBy: "本地模拟",
    updateTime: fmtDateTime(now),
  };
  store.rows.unshift(row);
  return row;
}

export function updateMockDesensWhitelist(data) {
  const rid = Number(data?.id);
  const idx = store.rows.findIndex((r) => Number(r.id) === rid);
  if (idx < 0) return null;
  const prev = store.rows[idx];
  const cat =
    store.cats.find((c) => String(c.code) === String(data?.dataCategoryCode)) ||
    null;
  const now = new Date();
  const next = {
    ...prev,
    ...data,
    dataCategoryName: cat?.name ?? prev.dataCategoryName,
    updateBy: "本地模拟",
    updateTime: fmtDateTime(now),
  };
  store.rows.splice(idx, 1, next);
  return next;
}

export function deleteMockDesensWhitelist(ids) {
  const arr = String(ids)
    .split(",")
    .map((s) => s.trim())
    .filter(Boolean)
    .map((v) => Number(v))
    .filter((v) => !Number.isNaN(v));
  if (!arr.length) return false;
  store.rows = store.rows.filter((r) => !arr.includes(Number(r.id)));
  return true;
}

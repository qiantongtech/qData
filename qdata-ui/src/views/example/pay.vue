<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <div class="payment-container">
    <h1>支付页面</h1>
    <form @submit.prevent="submitPayment">
      <div class="form-group">
        <label for="orderId">订单ID:</label>
        <input v-model="orderId" type="text" id="orderId" required />
      </div>
      <div class="form-group">
        <label for="amount">支付金额 (元):</label>
        <input v-model="amount" type="text" id="amount" required />
      </div>
      <div class="form-group">
        <label for="description">商品描述:</label>
        <input v-model="description" type="text" id="description" required />
      </div>
      <div class="form-group">
        <label for="paymentType">支付方式:</label>
        <select v-model="paymentType" id="paymentType" required>
          <option value="ALIPAY">支付宝</option>
          <option value="WECHAT">微信支付</option>
          <!-- 如果将来有其他支付方式，可以在这里添加 -->
        </select>
      </div>
      <button type="submit">提交支付</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import {pay} from "@/api/example/pay.js";

export default {
  data() {
    return {
      orderId: '',          // 订单ID
      amount: 0,            // 支付金额
      description: '',      // 商品描述
      paymentType: 'ALIPAY' // 默认支付方式为支付宝
    };
  },
  methods: {
    async submitPayment() {
      try {
        const paymentRequest = {
          orderId: this.orderId,
          amount: this.amount * 100,
          description: this.description,
          paymentType: this.paymentType, // 将用户选择的支付类型传递到后端
        };

        // 发送支付请求到后端
        pay(paymentRequest).then(response => {
          if (response.status === 'ALIPAY_SUCCESS') {
            // 将返回的支付表单插入到页面并提交
            const formContainer = document.createElement('div');
            formContainer.innerHTML = response.paymentUrl;
            document.body.appendChild(formContainer);
            formContainer.querySelector('form').submit(); // 自动提交表单
          } else if (response.status === 'WECHAT_SUCCESS') {
            alert('腾讯太坑，没有沙箱环境');
          } else {
            alert('暂不支持');
          }
        })
      } catch (error) {
        console.error('支付请求异常:', error);
        alert('支付请求异常，请重试！');
      }
    },
  },
};
</script>

<style scoped>
.payment-container {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f9f9f9;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
  border-radius: 4px;
  border: 1px solid #ccc;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #218838;
}
</style>

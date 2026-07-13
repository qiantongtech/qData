<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
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
          <!-- If there are other payment methods in the future, they can be added here -->
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
      orderId: '',          // Order ID
      amount: 0,            // Payment amount
      description: '',      // Product description
      paymentType: 'ALIPAY' // The default payment method is Alipay
    };
  },
  methods: {
    async submitPayment() {
      try {
        const paymentRequest = {
          orderId: this.orderId,
          amount: this.amount * 100,
          description: this.description,
          paymentType: this.paymentType, // Pass user selected payment type to backend
        };

        // Send payment request to backend
        pay(paymentRequest).then(response => {
          if (response.status === 'ALIPAY_SUCCESS') {
            // Insert the returned payment form into the page and submit it
            const formContainer = document.createElement('div');
            formContainer.innerHTML = response.paymentUrl;
            document.body.appendChild(formContainer);
            formContainer.querySelector('form').submit(); // Automatically submit a form
          } else if (response.status === 'WECHAT_SUCCESS') {
            alert('腾讯太坑，没有沙箱环境');
          } else {
            alert('暂不支持');
          }
        })
      } catch (error) {
        console.error("Payment request error:", error);
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

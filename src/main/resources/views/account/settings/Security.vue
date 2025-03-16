<template>
  <a-list itemLayout="horizontal">
    <a-list-item>
      <a-list-item-meta>
        <p slot="title">{{ title }}</p>
        <span slot="description">
          <span class="security-list-description">{{ description }}</span>
          <span v-if="action[index].value!=''"> : </span>
          <span class="security-list-value">{{ action[index].value }}</span>
        </span>
      </a-list-item-meta>
      <template>
        <a @click="action[index].callback">{{ action[index].title }}</a>
      </template>
    </a-list-item>
    <div id="horizontal" v-show="index&&start">
      <img style="width:180px" :src="src">
      <div class="account-well">
        <p class="prepend-top-0 append-bottom-0">
          无法扫描二维码？
        </p>
        <p class="prepend-top-0 append-bottom-0">
          如需手动添加条目，请在手机应用中提供以下信息。
        </p>
        <p class="prepend-top-0 append-bottom-0">
          帐户：{{username}}
        </p>
        <p class="prepend-top-0 append-bottom-0">
          密钥: {{secretKey}}
        </p>
        <p class="two-factor-new-manual-content">
          基于时间：是
        </p>
      </div>
    </div>
    <div v-show="index&&start">
      <div v-show="validate==1?true:false" class="alert alert-danger">
        无效的 pin 码
      </div>
      <h3>Pin码</h3>
      <a-input placeholder="请输入6位校验码" v-model="pin"/>&nbsp;
      <a-button style="margin-top: 13px;margin-left: -6px;" type="primary" @click="validateKey">使用双重认证应用注册</a-button>
    </div>
    <div v-show="validate==2?true:false&&start" class="alert alert-success">
      恭喜！您已启用了双重认证！
    </div>
  </a-list>
</template>

<script>
  import Vue from 'vue'
  import {ACCESS_TOKEN, ENCRYPTED_STRING, USER_INFO} from "@/store/mutation-types"
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction, postAction} from "@/api/manage";

  export default {

    mixins: [JeecgListMixin],
    data() {
      return {
        title: '双重认证',
        description: '启用双重身份认证（2FA），提高账户安全性',
        username: Vue.ls.get(USER_INFO)["username"],
        src: "",
        pin: "",
        secretKey: "",
        validate: 0,
        start: 0,
        action: [{
          title: "开启",
          callback: () => {
            getAction("/auth/generate/", {username: this.username}).then((res) => {
              this.src = res["src"]
              this.secretKey = res["secretKey"]
            })
            this.index = 1
            this.start = 1
          },
          value: "未启用",
        }, {
          title: "关闭",
          callback: () => {
            postAction("/auth/close/", {username: this.username}).then((res) => {
              if (res) { //true 验证成功
                this.$message.info('已关闭');
              }
              this.index = 0
              this.validate = 0  //重置pin校验
            })
          },
          value: "已开启",
        }],
        index: 0  //0 未启用, 1 启用
      }
    },
    methods: {
      validateKey() { // 校验pin码
        postAction("/auth/validate/", {
          username: this.username,
          secretKey: this.secretKey,
          pin: this.pin
        }).then((res) => {
          if (res) { //true 验证成功
            this.validate = 2
            this.start = 0
            this.$message.info('已开启');
          } else {
            this.validate = 1
          }
        })
      }
    },
    created() {
      postAction("/auth/isTFA/", {
        username: Vue.ls.get(USER_INFO)["username"]
      }).then((res) => {
        if (res) { //true 成功
          this.index = 1  // 已经启用
        } else {
          this.index = 0  // 未启用
        }
      });
    }
  }
</script>

<style scoped>
  .account-well {
    padding: 10px;
    background-color: #fafafa;
    border: 1px solid #e5e5e5;
    border-radius: 3px;
    width: 350px;
  }

  .append-bottom-0 {
    margin-bottom: 0;
  }

  .prepend-top-0 {
    margin-top: 0;
  }

  #horizontal {
    display: flex;
  }

  .alert-success {
    background-color: #21a6db;
    border-color: #21a5da;
  }

  .alert-danger {
    background-color: #db3b21;
    border-color: #db3b21;
    color: #fff;
  }

  .alert {
    border-radius: 0;
    width: 530px;
  }
</style>
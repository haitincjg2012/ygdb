'use strict';

var router;
router = require('express').Router();
module.exports = router;

router.use(require('./user-product-his'));
router.use(require('./user-captcha'));
router.use(require('./user-info'));
router.use(require('./user-view'));
router.use(require('./user-messagecount'));
router.use(require('./user-productsearch-clear'));
router.use(require('./user-productsearch-his'));
router.use(require('./user-productsearch'));
router.use(require('./user-checkproduct'));
router.use(require('./user-saveproduct'));
router.use(require('./user-address-his'));
router.use(require('./user-address-save'));
router.use(require('./user-sign'));
router.use(require('./user-checksign'));

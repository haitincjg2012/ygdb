'use strict';

var router;
router = require('express').Router();
module.exports = router;

router.use(require('./product-list'));
router.use(require('./product-detail'));
router.use(require('./product-view'));
router.use(require('./product-user-list'));
router.use(require('./product-user-push-list'));
router.use(require('./product-user-off-list'));
router.use(require('./product-recommend'));

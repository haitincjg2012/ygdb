'use strict';

var router;
router = require('express').Router();
module.exports = router;

router.use(require('./org-view'));
router.use(require('./org-viewinfo'));
router.use(require('./org-attention'));
router.use(require('./org-attenflag'));
router.use(require('./org-depot-list'));
router.use(require('./org-product-list'));
router.use(require('./org-agency-list'));
router.use(require('./org-merchant-list'));

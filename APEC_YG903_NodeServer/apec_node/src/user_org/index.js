'use strict';

var router;
router = require('express').Router();
module.exports = router;

router.use(require('./org-view'));
router.use(require('./org-depot-list'));
router.use(require('./org-agency-list'));
router.use(require('./org-merchant-list'));

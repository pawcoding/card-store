## [1.4.3](https://github.com/pawcoding/card-store/compare/v1.4.2...v1.4.3) (2026-02-19)


### Bug Fixes

* **proguard:** keep mlkit to not crash on startup ([6e962ab](https://github.com/pawcoding/card-store/commit/6e962abb6b9fc3de7f89886e4e673fe0f0def65b))

## [1.4.2](https://github.com/pawcoding/card-store/compare/v1.4.1...v1.4.2) (2025-12-12)

## [1.4.1](https://github.com/pawcoding/card-store/compare/v1.4.0...v1.4.1) (2025-11-22)

# [1.4.0](https://github.com/pawcoding/card-store/compare/v1.3.0...v1.4.0) (2025-10-10)


### Bug Fixes

* **edit-label:** correctly detect unsaved changes to a label ([9b47d82](https://github.com/pawcoding/card-store/commit/9b47d822d860be728f654081fee550a253942e6a))


### Features

* **edit:** add back button support for unsaved changes dialog ([#42](https://github.com/pawcoding/card-store/issues/42)) ([8d394df](https://github.com/pawcoding/card-store/commit/8d394df870e0bdc2814fa2d60b36202e20506f81))
* **auth:** add biometric authentication ([6fbfcba](https://github.com/pawcoding/card-store/commit/6fbfcbad68b22701e89b367d5e84ac0a7de15ab8))
* **edit:** add dialog to handle unsaved changes with dismiss and save options ([a33b5ea](https://github.com/pawcoding/card-store/commit/a33b5ea88f50e2adc659ef512dda3d2347ed0047))
* **backup:** enable full app backups ([ee80dbc](https://github.com/pawcoding/card-store/commit/ee80dbcfe9dec9dba86d65dd4a276965808f8e95)), closes [#45](https://github.com/pawcoding/card-store/issues/45)
* **about:** redesign page ([#46](https://github.com/pawcoding/card-store/issues/46)) ([1e19667](https://github.com/pawcoding/card-store/commit/1e196678990358b9454dd13b635fb80a460e8953))

# [1.3.0](https://github.com/pawcoding/card-store/compare/v1.2.1...v1.3.0) (2025-09-19)


### Features

* **about:** add link to PlayStore page [build] ([f86965c](https://github.com/pawcoding/card-store/commit/f86965c6c4843b95f53b72bb94ca496638234554))
* **navigation:** opt-in to predictive back gestures on Android 15 and lower ([6582b88](https://github.com/pawcoding/card-store/commit/6582b88ff90803470e87d6c5e428356ef8d535d0))
* **import:** update card with import when it already exists ([2938037](https://github.com/pawcoding/card-store/commit/29380378b704c03046281d8328dadb7c05b6daf4))

## [1.2.1](https://github.com/pawcoding/card-store/compare/v1.2.0...v1.2.1) (2025-07-17)

# [1.2.0](https://github.com/pawcoding/card-store/compare/v1.1.0...v1.2.0) (2025-05-31)


### Bug Fixes

* update UI on larger font sizes ([7f9e578](https://github.com/pawcoding/card-store/commit/7f9e578a3c4d9daace7ca3e5a4bef2ed6a70ecf9))


### Features

* **card-list:** add "import pkpass file" option when adding card ([585a879](https://github.com/pawcoding/card-store/commit/585a8796807eee9420cf0517ed94ed781d2aaaed))
* **sort:** add "intelligent" sort option that scores the cards usage ([27707ad](https://github.com/pawcoding/card-store/commit/27707adf99b7af6a9fd8f4d09fb27221e61ff9d5))
* **sort:** add icons to sort dropdown ([ab796ff](https://github.com/pawcoding/card-store/commit/ab796ffc3eb68326d6a925e7581b952be4de99df))
* **review:** add in-app review request ([70dc659](https://github.com/pawcoding/card-store/commit/70dc659f3b40bd1977ad0ecf71e859e5f5309d9c))
* **import:** animate card to import ([9d182cb](https://github.com/pawcoding/card-store/commit/9d182cb8f2ce9a50b42f320f79573879be19affa))
* **pkpass:** parse .pkpass content ([79388ef](https://github.com/pawcoding/card-store/commit/79388ef674805bf795fabb029fd30a28f3461d4c))
* **pkpass:** read content from pkpass file ([8be0446](https://github.com/pawcoding/card-store/commit/8be0446226048b7c7bee7e96c309fe03d07efcc0))

# [1.1.0](https://github.com/pawcoding/card-store/compare/v1.0.0...v1.1.0) (2025-05-21)


### Bug Fixes

* **code-scanner:** correctly handle scanned codes with umlauts ([b73327d](https://github.com/pawcoding/card-store/commit/b73327d0e4c5956c861dec46b7617aaada945fa1))
* **cards-list:** prevent crashes when updating sort attribute ([10309e8](https://github.com/pawcoding/card-store/commit/10309e8a82151b834db5a1618b86b6e06b322404))
* **cards-list:** prevent crashes when updating sort attribute ([689e6e9](https://github.com/pawcoding/card-store/commit/689e6e98f529f3f758fd1d703ca9b58fba7995e6))
* **snackbar:** use correct colors for snackbar ([c4ac047](https://github.com/pawcoding/card-store/commit/c4ac0471ca90ad356214bd9fe1f2d8d291e27d3e))


### Features

* add haptic feedback for long click and card scan ([36228bc](https://github.com/pawcoding/card-store/commit/36228bc55e54577fe49bd6aac6f5b210d0de2664))
* **cards-list:** add virtual card to add a new card ([d31b217](https://github.com/pawcoding/card-store/commit/d31b217b668f40e89c5aaa45c23de76be0f0cdc4))
* **options-sheet:** display info about item ([840ede9](https://github.com/pawcoding/card-store/commit/840ede94a7a62325acb9e83a2099a5b9ce19844e))
* **label-list-screen:** enable swipe actions for labels ([a1dca3f](https://github.com/pawcoding/card-store/commit/a1dca3fef08024bddf9b3bd85d0dfaa2293f7c3f))
* **share:** share and import card via deeplink ([62951ce](https://github.com/pawcoding/card-store/commit/62951cee0acae16c0ff4d6a6ca382712e1b5811a))
* **share:** show bottom sheet with qr code ([f4c3922](https://github.com/pawcoding/card-store/commit/f4c39220affd248bac21840a512582d64bfbd1e3))
* **import:** show special "import a card" sheet ([25606b9](https://github.com/pawcoding/card-store/commit/25606b93c4c2c0ae20200888979aa91ef86ff603))
* **i18n:** support per-app language preference ([6da4c59](https://github.com/pawcoding/card-store/commit/6da4c5925de3ff03aad7cd1026c98f78b3a77629))
* **about:** update about page ([7f4c3f3](https://github.com/pawcoding/card-store/commit/7f4c3f34597ab9b3ef6e5ee7c6fd840dbe96a585))

# 1.0.0 (2025-05-04)


### Bug Fixes

* **label-list:** add scrolling to list ([2113035](https://github.com/pawcoding/card-store/commit/2113035c7f26ea1ae8e83d865badb43071e6a97e))
* **edit-card:** allow users to edit newly created cards after scanning ([7d5d385](https://github.com/pawcoding/card-store/commit/7d5d385bf06011d58e2b6493f43e5ea38df5df96))
* **card-scanner:** copy error to clipboard ([525e886](https://github.com/pawcoding/card-store/commit/525e8861db60a0ee56d0f98039b916ea6957a867))
* **edit-card:** display hint when no labels exist yet ([ea796ea](https://github.com/pawcoding/card-store/commit/ea796ea003fd71e49d28777615731a423f82d4dd))
* **card-list:** fill whole screen with cards to correctly render shadows ([896b41f](https://github.com/pawcoding/card-store/commit/896b41fad4f941e76ac43b7479f8b4b762afa558))
* **card-edit:** how initial card data when editing a card ([3f2234e](https://github.com/pawcoding/card-store/commit/3f2234e2caf7fa8e9454c29ca45b0ede3be28043))
* **cards-list:** prevent flickering when starting app ([2e7c87d](https://github.com/pawcoding/card-store/commit/2e7c87d360c0c1a4b5b12b29736559905bc44745))
* **barcode-scanner:** request barcode module if not available ([11d8c3e](https://github.com/pawcoding/card-store/commit/11d8c3e00452d4c66905f6372105e021f00d5f42))
* **edit-card:** show toast after saving modified card instead of closing instantly ([cbbea13](https://github.com/pawcoding/card-store/commit/cbbea135cd07c512de75bad834c91f85d99fce67))
* **card-list:** update state when cards are changed ([8e4dbc5](https://github.com/pawcoding/card-store/commit/8e4dbc51b672d61db96ea37ac7e829dd585138d1))
* **barcode-scanner:** use separate barcode modules for availability check and scanning ([dd29558](https://github.com/pawcoding/card-store/commit/dd29558137a6ada70b78701b0ee83ce165223563))


### Features

* **about:** add about page with useful links ([31a112b](https://github.com/pawcoding/card-store/commit/31a112bbd90c60e7c29a76483cc17ebf89bee10a))
* **scanner:** add barcode scanner to auto-fill card infos ([199f670](https://github.com/pawcoding/card-store/commit/199f6708bb1f2a17ae7f126fe01ded4c803d7ecf))
* **edit-card:** add color picker ([fb3b545](https://github.com/pawcoding/card-store/commit/fb3b545683ec13e4610478d9734c72202fa60cad))
* **edit-label:** add editor for labels ([2ae9b06](https://github.com/pawcoding/card-store/commit/2ae9b06c9031e5600f36ba82f4b78c9d72c5ca61))
* **color-picker:** add hex-code input to specify exact color ([12309e3](https://github.com/pawcoding/card-store/commit/12309e37d4be3d63caa691e4d4e68c9367e598a7))
* **branding:** add icon and spash screen ([a6f4180](https://github.com/pawcoding/card-store/commit/a6f418080f183da24301f8742e78e39288b880cf))
* add initial POC ([47b1629](https://github.com/pawcoding/card-store/commit/47b16297880fcbf8b9e1144c3905c0750de4df01))
* **labels:** add label filter list ([05ed25e](https://github.com/pawcoding/card-store/commit/05ed25e116707b0a83c4012412ffbfcdfe245f02))
* **labels:** add list to view all labels ([9978b88](https://github.com/pawcoding/card-store/commit/9978b8813932d0214452ead03a8b5a0bbbe992ee))
* **card:** add modal to show QR code in full-screen ([2b7b7d6](https://github.com/pawcoding/card-store/commit/2b7b7d680aed424714423d2e64b76b4673bf9f4b))
* **card-list:** add option to delete cards ([2c74829](https://github.com/pawcoding/card-store/commit/2c74829de6340810dce09e8e501b21f41841c2b4))
* **create-card:** add option to directly scan card ([0d3e920](https://github.com/pawcoding/card-store/commit/0d3e9203e874f421b81bd2759f5a7d1fc1dffa08))
* **card-list-screen:** add options to sort cards ([47cd3df](https://github.com/pawcoding/card-store/commit/47cd3df2f0b54e9d76a59d867502cca0a933c242))
* **card:** add sheet to show QR code of card ([8707339](https://github.com/pawcoding/card-store/commit/87073396fdb1dae968e08b62e1abec250eb43767))
* **navigation:** add simple slide animations ([5c96d12](https://github.com/pawcoding/card-store/commit/5c96d12f86470ccc5c30cc98e6de7e2e1452b7aa))
* **edit-card:** edit labels for card ([165d0c5](https://github.com/pawcoding/card-store/commit/165d0c5045fd9656cb00685646328620a96202aa))
* **cards-list:** filter cards by their labels ([bc372e5](https://github.com/pawcoding/card-store/commit/bc372e5fec01ab68ce04e2cc6d0f86843a4c1155))
* **card-options:** introduce general OptionSheet to display actions ([67ead90](https://github.com/pawcoding/card-store/commit/67ead900fcf78d121282fbd0ffedbd6238077dd9))
* **barcode-scanner:** request background installation on startup ([73ad331](https://github.com/pawcoding/card-store/commit/73ad3316ca6235f6b0d0fb2ad29ebb81d34f9bef))
* **card-list-screen:** save last selected sort attribute ([2d83658](https://github.com/pawcoding/card-store/commit/2d836583856f4b1d24e8c555fc2c8661f4ee5062))
* **cards-list:** show hint to add first card ([269a088](https://github.com/pawcoding/card-store/commit/269a0889fe392dc7cf1a4a1d9a5fdf3587c52cb8))
* **color-picker:** show preview of selected color ([72b0647](https://github.com/pawcoding/card-store/commit/72b064741e264ccf83c3639840b57fb26503ff0f))
* **edit-card:** show selected labels for card ([ad21e97](https://github.com/pawcoding/card-store/commit/ad21e9709ef7f0075513acd3af22c35a045c25a5))
* **edit-card:** show warning when barcode type is not compatible ([703ce55](https://github.com/pawcoding/card-store/commit/703ce5526aae6f43088ecfbc90c8d83399a63ac9))
* **card:** support multiple barcode formats ([84f8b37](https://github.com/pawcoding/card-store/commit/84f8b378524a140d108b053a7b40d5e0a214eccc))
* **card-view:** track usages of cards ([99dfeea](https://github.com/pawcoding/card-store/commit/99dfeea1afd7b1a3cbc8108b578c419bb98b1901))
* **list-cards,list-labels:** update empty lists ([70b6c79](https://github.com/pawcoding/card-store/commit/70b6c797d7eefaffcc9f69e3729db20f28fb2ac0))

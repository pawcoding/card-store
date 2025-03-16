const branch = process.env.GITHUB_REF_NAME;

const assetsToUpdate = ['version.properties'];
if (branch === 'master') {
  assetsToUpdate.push('CHANGELOG.md');
}

const config = {
  branches: ['master', { name: 'staging', channel: 'next', prerelease: true }],
  plugins: [
    [
      '@semantic-release/commit-analyzer',
      {
        preset: 'angular',
        releaseRules: [
          { type: 'docs', scope: 'README', release: 'patch' },
          { type: 'build', scope: 'deps', release: 'patch' },
          { type: 'refactor', release: 'patch' },
          { type: 'style', release: 'patch' }
        ],
        parserOpts: {
          noteKeywords: ['BREAKING CHANGE', 'BREAKING CHANGES']
        }
      }
    ],
    [
      '@semantic-release/release-notes-generator',
      {
        preset: 'angular',
        parserOpts: {
          noteKeywords: ['BREAKING CHANGE', 'BREAKING CHANGES']
        },
        writerOpts: {
          commitsSort: ['subject', 'scope']
        }
      }
    ],
    '@semantic-release/changelog',
    [
      '@semantic-release/git',
      {
        assets: assetsToUpdate
      }
    ],
    [
        '@semantic-release/github',
        {
            assets: ['app/build/outputs/apk/release/app-release.apk']
        }
    ],
    [
      '@semantic-release/exec',
      {
        prepareCmd: './scripts/build-apk.sh ${nextRelease.version}'
      }
    ]
  ]
};

module.exports = config;
const branch = process.env.GITHUB_REF_NAME;
const dryRun = process.env.DRY_RUN;

const assetsToUpdate = ["version.properties"];
if (branch === "master") {
  assetsToUpdate.push("CHANGELOG.md");
}

const config = {
  branches: ["master", { name: "staging", channel: "next", prerelease: 'next' }],
  plugins: [
    [
      "@semantic-release/commit-analyzer",
      {
        preset: "angular",
        releaseRules: [
          { breaking: true, release: "major" },
          { type: "build", scope: "deps", release: "patch" },
          { type: "refactor", release: "patch" },
          { type: "ci", release: "patch" },
          { type: "i18n", release: "patch" }
        ],
        parserOpts: {
          noteKeywords: ["BREAKING CHANGE", "BREAKING CHANGES"]
        }
      }
    ],
    [
      "@semantic-release/release-notes-generator",
      {
        preset: "conventionalcommits",
        parserOpts: {
          noteKeywords: ["BREAKING CHANGE", "BREAKING CHANGES"]
        },
        presetConfig: {
          types: [
            { type: "feat", section: "🚀 Features" },
            { type: "fix", section: "🩹 Bug Fixes" },
            { type: "perf", section: "⚡ Performance Improvements" },
            { type: "revert", section: "↩️ Reverts" },
            { type: "docs", section: "📖 Documentation" },
            { type: "refactor", section: "🛠️ Code Refactoring" },
            { type: "test", section: "🧪 Tests" },
            { type: "i18n", section: "🌐 Internationalization" },
            { type: "build", scope: "deps", section: "🏗 Dependency updates" },
            { type: "build", hidden: true },
            { type: "ci", section: "🔧 Continuous Integration" }
          ]
        }
      }
    ],
    "@semantic-release/changelog",
    [
      "@semantic-release/exec",
      {
        prepareCmd: dryRun
          ? undefined
          : "./scripts/update-version.sh ${nextRelease.version}",
        publishCmd: dryRun ? undefined : "./scripts/build-apk.sh"
      }
    ],
    [
      "@semantic-release/git",
      {
        assets: assetsToUpdate
      }
    ],
    [
      "@semantic-release/github",
      {
        assets: [
          {
            path: "app/build/outputs/apk/release/*.apk",
            name: "CardStore-${nextRelease.gitTag}.apk",
            label: "CardStore (${nextRelease.version})"
          }
        ],
        successCommentCondition:
          '<% return issue.pull_request || !nextRelease.channel || !issue.labels.some(label => label.name === "released on @next"); %>'
      }
    ]
  ]
};

module.exports = config;

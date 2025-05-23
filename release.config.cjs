const branch = process.env.GITHUB_REF_NAME;

const assetsToUpdate = ["version.properties"];
if (branch === "master") {
  assetsToUpdate.push("CHANGELOG.md");
}

const config = {
  branches: ["master", { name: "staging", channel: "next", prerelease: true }],
  plugins: [
    [
      "@semantic-release/commit-analyzer",
      {
        preset: "angular",
        releaseRules: [
          { type: "docs", scope: "README", release: "patch" },
          { type: "build", scope: "deps", release: "patch" },
          { type: "refactor", release: "patch" },
          { type: "ci", release: "patch" },
          { type: "i18n", release: "patch" },
          { type: "style", release: "patch" }
        ],
        parserOpts: {
          noteKeywords: ["BREAKING CHANGE", "BREAKING CHANGES"]
        }
      }
    ],
    [
      "@semantic-release/release-notes-generator",
      {
        preset: "angular",
        parserOpts: {
          noteKeywords: ["BREAKING CHANGE", "BREAKING CHANGES"]
        },
        writerOpts: {
          commitsSort: ["subject", "scope"]
        }
      }
    ],
    "@semantic-release/changelog",
    [
      "@semantic-release/exec",
      {
        prepareCmd: "./scripts/update-version.sh ${nextRelease.version}",
        publishCmd: "./scripts/build-apk.sh"
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

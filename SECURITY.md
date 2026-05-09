# Security Policy

Thanks for helping keep dante-engine and the wider [dante-compass](https://github.com/dante-compass) ecosystem secure.

## Reporting a vulnerability

**Please do not open a public issue, discussion, or pull request that describes a suspected vulnerability** — that gives attackers a head-start before a fix lands.

Use one of these private channels instead:

1. **GitHub's Private Vulnerability Reporting (preferred).**
   Open a private security advisory at <https://github.com/dante-compass/dante-engine/security/advisories/new>.
   GitHub automatically routes it to the maintainers, keeps the discussion private until you and the maintainers agree to publish, and (optionally) assigns a CVE on publish.

2. **Email.**
   Email reporting is not available yet; please use GitHub PVR.

## What to include

A good report contains:

- A clear description of the vulnerability and its impact.
- Steps to reproduce against a specific commit SHA or release tag.
- Affected code locations (`file:line`) where useful.
- A suggested fix or mitigation if you have one.
- Whether you'd like credit in the published advisory and, if so, under what name.

## Scope and supported versions

dante-engine is the foundational library for dante-cloud, providing core implementations of OAuth2/OpenID Connect flows, token handling (JWT and opaque), XSS defences, cryptographic utilities, and database interaction helpers. As a shared component, its security posture directly affects all consumers.

The maintainers support security fixes on the latest 4.x minor release . Older releases will not generally receive backports.

In-scope vulnerability classes include:

- Authentication / authorization bypass in core filters, interceptors, or decision components
- OAuth2 / OIDC protocol implementation flaws (PKCE, redirect-URI validation, scope escalation, state/nonce handling, client authentication)
- JWT signature / claims validation flaws
- Opaque token handling flaws – missing or insufficient validation of introspection responses, token replay, scope mismatch, SSRF vectors in introspection endpoints
- Session fixation, social-login account linking flaws
- Cross-site scripting (XSS) – reflected or stored XSS in server-rendered content (error messages, default templates, helper views) or in any bundled UI components, including bypasses of the library's built-in XSS protections
- SSRF in IdP / userinfo endpoint fetching or any configurable outbound requests
- SQL injection in dynamic query builders, criteria helpers, or any concatenated query paths
- Insecure deserialization in token codecs or other object deserialization paths
- Cryptographic misuse – hardcoded keys, weak algorithms, insecure random generation, improper storage of signing secrets
- CSRF in state-changing endpoints or actions exposed by default configurations
- Dependency confusion / supply chain attacks targeting the library's own artifacts (please report privately if you suspect a tampered release)

Out-of-scope:

- Findings against intentionally trusted-admin features (config-driven behaviour that the framework explicitly delegates to the integrator).
- Issues that require an attacker to already have full database / server access.
- Best-practice complaints without a concrete impact (e.g. "this header should be set", "TLS version should be raised").

## Process

After you submit:

1. A maintainer will acknowledge receipt within roughly 1 week.
2. We'll triage the report: confirm severity, scope, and reproducibility.
3. We'll work with you on a fix and a coordinated disclosure timeline (typically up to 90 days, longer if the fix is structural).
4. On publication, we credit you in the advisory unless you ask not to be credited.

## Hall of fame

Reporters who have helped harden dante-cloud via responsible disclosure will be listed here once the corresponding advisory is published.

---

This policy is suggested via [GitHub's "Suggest a security policy" workflow](https://docs.github.com/en/code-security/getting-started/adding-a-security-policy-to-your-repository). Maintainers can edit any section freely; the most important thing is that **a private reporting channel exists** so researchers can submit findings responsibly.
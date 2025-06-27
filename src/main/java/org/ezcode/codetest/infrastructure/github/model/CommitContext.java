package org.ezcode.codetest.infrastructure.github.model;

public record CommitContext(

    String headCommitSha,

    String baseTreeSha

) {
}

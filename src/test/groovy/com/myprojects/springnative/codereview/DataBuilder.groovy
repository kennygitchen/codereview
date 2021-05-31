package com.myprojects.springnative.codereview

import com.myprojects.springnative.codereview.api.request.ReviewRequest
import com.myprojects.springnative.codereview.core.domain.Applicant
import com.myprojects.springnative.codereview.core.domain.Position
import com.myprojects.springnative.codereview.core.domain.Review
import com.myprojects.springnative.codereview.core.domain.Reviewer

import java.sql.Timestamp
import java.time.LocalDateTime

class DataBuilder {

    static Review review(Long id, Applicant applicant, Reviewer reviewer) {
        return new Review(
                id: id,
                applicant: applicant,
                assessedBy: reviewer,
                scores: [],
                feedback: "test feedback",
                comments: "test comments",
                dateSubmitted: Timestamp.valueOf("2020-01-01 10:10:10")
        )
    }

    static Applicant applicant(Long id) {
        new Applicant(id, "applicant", "applicant@gmail.com", "0433211211", new Position("Senior"), [])
    }

    static Reviewer reviewer(Long id) {
        new Reviewer(id, "Engineer", new Position("Senior"))
    }


    static ReviewRequest reviewRequest(Long applicantId, Long reviewerId, List<ReviewRequest.ScoreRequest> scores) {
        return new ReviewRequest(
                applicantId,
                reviewerId,
                scores,
                'some feedback',
                'some comments',
                LocalDateTime.now()
        )
    }

    static ReviewRequest.ScoreRequest scoreRequest(Long id, Double score = 3) {
        return new ReviewRequest.ScoreRequest(id, """some criteria comments for $id""", score)
    }
}

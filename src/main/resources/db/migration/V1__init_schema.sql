CREATE TABLE superadmins (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE units (
    id          BIGSERIAL PRIMARY KEY,
    unit_number INT NOT NULL UNIQUE CHECK (unit_number > 0),
    title       VARCHAR(255) NOT NULL,
    objective   TEXT NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE subtopics (
    id         BIGSERIAL PRIMARY KEY,
    unit_id    BIGINT NOT NULL REFERENCES units(id) ON DELETE CASCADE,
    code       VARCHAR(20) NOT NULL CHECK (code ~ '^[0-9]+(\.[0-9]+)+$'),
    title      VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_subtopic_code_per_unit UNIQUE (unit_id, code)
);

CREATE INDEX idx_subtopics_unit_id ON subtopics(unit_id);

CREATE TABLE subtopic_content (
    id             BIGSERIAL PRIMARY KEY,
    subtopic_id    BIGINT NOT NULL UNIQUE REFERENCES subtopics(id) ON DELETE CASCADE,
    html_body      TEXT NOT NULL,
    apa_references TEXT,
    extra_data     TEXT,
    media_url      VARCHAR(500),
    media_type     VARCHAR(20) NOT NULL DEFAULT 'NONE'
                   CHECK (media_type IN ('IMAGE', 'VIDEO', 'NONE')),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_media_url_required
        CHECK (media_type = 'NONE' OR media_url IS NOT NULL)
);

CREATE TABLE reinforcement_cards (
    id          BIGSERIAL PRIMARY KEY,
    subtopic_id BIGINT NOT NULL REFERENCES subtopics(id) ON DELETE CASCADE,
    title       VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_cards_subtopic_id ON reinforcement_cards(subtopic_id);

CREATE TABLE questions (
    id          BIGSERIAL PRIMARY KEY,
    subtopic_id BIGINT NOT NULL REFERENCES subtopics(id) ON DELETE CASCADE,
    reagent     TEXT NOT NULL,
    explanation TEXT NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_questions_subtopic_id ON questions(subtopic_id);

CREATE TABLE question_options (
    id          BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    option_text TEXT NOT NULL,
    is_correct  BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_options_question_id ON question_options(question_id);

CREATE UNIQUE INDEX uq_one_correct_option
    ON question_options(question_id)
    WHERE is_correct;

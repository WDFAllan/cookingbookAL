CREATE TABLE IF NOT EXISTS recipe_rating (
    id        BIGSERIAL PRIMARY KEY,
    recipe_id INTEGER NOT NULL REFERENCES recette(id) ON DELETE CASCADE,
    user_id   BIGINT  NOT NULL REFERENCES users(id),
    rating    INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    UNIQUE (recipe_id, user_id)
);
